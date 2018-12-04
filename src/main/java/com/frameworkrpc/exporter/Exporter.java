package com.frameworkrpc.exporter;

public interface Exporter {

	Exporter openServer();

	Exporter exporter();

	Exporter unexport();
}
