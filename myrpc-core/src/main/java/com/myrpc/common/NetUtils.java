package com.myrpc.common;

import com.myrpc.exception.MyRpcRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class NetUtils {

	private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);

	private static volatile InetAddress inetAddress;
	private static volatile int port;

	public static final String ANYHOST = "0.0.0.0";
	public static final String LOCALHOST = "127.0.0.1";
	private static volatile InetAddress LOCAL_ADDRESS = null;

	private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

	private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

	private static final ReentrantLock hostLock = new ReentrantLock();
	private static final ReentrantLock portLock = new ReentrantLock();

	private static boolean isValidAddress(InetAddress address) {
		if (address == null || address.isLoopbackAddress())
			return false;
		String name = address.getHostAddress();
		return (name != null && !ANYHOST.equals(name) && !LOCALHOST.equals(name) && IP_PATTERN.matcher(name).matches());
	}

	public static boolean isLocalHost(String host) {
		return host != null && (LOCAL_IP_PATTERN.matcher(host).matches() || host.equalsIgnoreCase("localhost"));
	}

	public static boolean isAnyHost(String host) {
		return "0.0.0.0".equals(host);
	}


	/**
	 *获取可用InetAddress
	 * @return
	 */
	public static InetAddress getAvailablInetAddress() {
		if (inetAddress == null) {
			try {
				hostLock.lock();
				if (inetAddress != null) {
					return inetAddress;
				}
				InetAddress localAddress = getLocalAddress0();
				inetAddress = localAddress;
			} finally {
				hostLock.unlock();
			}
		}
		if (inetAddress == null) {
			throw new MyRpcRpcException("can not find inetAddress");
		}
		return inetAddress;
	}

	/**
	 *获取可用端口号
	 * @return
	 */
	public static int getAvailablePort() {
		if (port == 0) {
			try {
				portLock.lock();
				int _port = RpcConstants.DEFAULT_IOPORT;
				int i = 100;
				while (i > 0) {
					i--;
					try (ServerSocket socket = new ServerSocket(_port)) {
						port = _port;
						break;
					} catch (IOException e) {
						_port++;
						port = 0;
					}
				}
			} finally {
				portLock.unlock();
			}
		}
		if (port == 0)
			throw new MyRpcRpcException("can not find availablePort");
		return port;
	}

	public static String getLocalHost() {
		InetAddress address = getLocalAddress();
		return address == null ? LOCALHOST : address.getHostAddress();
	}

	public static InetAddress getLocalAddress() {
		if (LOCAL_ADDRESS != null)
			return LOCAL_ADDRESS;
		InetAddress localAddress = getLocalAddress0();
		LOCAL_ADDRESS = localAddress;
		return localAddress;
	}

	private static InetAddress getLocalAddress0() {
		InetAddress localAddress = null;
		try {
			localAddress = InetAddress.getLocalHost();
			if (isValidAddress(localAddress)) {
				return localAddress;
			}
		} catch (Throwable e) {
			logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
		}
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			if (interfaces != null) {
				while (interfaces.hasMoreElements()) {
					try {
						NetworkInterface network = interfaces.nextElement();
						Enumeration<InetAddress> addresses = network.getInetAddresses();
						if (addresses != null) {
							while (addresses.hasMoreElements()) {
								try {
									InetAddress address = addresses.nextElement();
									if (isValidAddress(address)) {
										return address;
									}
								} catch (Throwable e) {
									logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
								}
							}
						}
					} catch (Throwable e) {
						logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
					}
				}
			}
		} catch (Throwable e) {
			logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
		}
		logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
		return localAddress;
	}


	static boolean isValidV6Address(Inet6Address address) {
		boolean preferIpv6 = Boolean.getBoolean("java.net.preferIPv6Addresses");
		if (!preferIpv6) {
			return false;
		}
		try {
			return address.isReachable(100);
		} catch (IOException e) {
			// ignore
		}
		return false;
	}


	static InetAddress normalizeV6Address(Inet6Address address) {
		String addr = address.getHostAddress();
		int i = addr.lastIndexOf('%');
		if (i > 0) {
			try {
				return InetAddress.getByName(addr.substring(0, i) + '%' + address.getScopeId());
			} catch (UnknownHostException e) {
				// ignore
				logger.debug("Unknown IPV6 address: ", e);
			}
		}
		return address;
	}

	/**
	 * 将url参数转换成map
	 * @param param aa=11&bb=22&cc=33
	 * @return
	 */
	public static Map<String, String> getUrlParams(String param) {
		Map<String, String> map = new HashMap<String, String>(0);
		if (StringUtils.isEmpty(param)) {
			return map;
		}
		String[] params = param.split("&");
		for (int i = 0; i < params.length; i++) {
			String[] p = params[i].split("=");
			if (p.length == 2) {
				map.put(p[0], p[1]);
			}
		}
		return map;
	}

	/**
	 * 将map转换成url
	 * @param map
	 * @return
	 */
	public static String getUrlParamsByMap(Map<String, String> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append("&");
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
}
