package com.frameworkrpc.exporter;

import com.frameworkrpc.model.URL;

public interface Exporter {

	URL getUrl();

	void exportServer();

	void exportUrl();

	void unexportServer();

	void unexportUrl();
}
