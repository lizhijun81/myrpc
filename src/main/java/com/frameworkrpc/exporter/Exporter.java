package com.frameworkrpc.exporter;

import com.frameworkrpc.model.URL;

public interface Exporter {

	void setUrl(URL url);

	URL getUrl();

	void initExporter();

	void exportServer();

	void exportUrl();

	void unexportServer();

	void unexportUrl();
}
