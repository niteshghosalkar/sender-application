package com.sender.app.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sender.app.exception.SenderException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SenderServiceImpl implements SenderService {

	@Value("${buffer_size}")
	private Integer buffer_size;

	@Value("${port}")
	private Integer port;

	@Value("#{environment['path_to_read']}")
	private String path_to_read;

	public void process() {
		// TODO Auto-generated method stub
		log.info("Sender Service Started");
		try {
			SocketChannel socketChannel = CreateChannel();
			sendFile(socketChannel);
		} catch (SenderException se) {
			log.error("", se);
		}
		log.info("Sender Service Finished");
	}

	private void sendFile(SocketChannel socketChannel) {

		// Read a file from disk. Its filesize is 54.3 MB (57,006,053 bytes)
		// receive the same size 54.3 MB (57,006,053 bytes)
		try {
			log.debug("Read file from {}", path_to_read);
			if (StringUtils.isEmpty(path_to_read) == Boolean.FALSE) {
				Path path = Paths.get(path_to_read);// "c:\\sample.txt"
				FileChannel inChannel = FileChannel.open(path);

				ByteBuffer buffer = ByteBuffer.allocate(buffer_size);
				while (inChannel.read(buffer) > 0) {
					buffer.flip();
					socketChannel.write(buffer);
					buffer.clear();
				}
			} else {
				throw new SenderException("ENV variable path_to_read Not provided");
			}

		} catch (IOException ioe) {
			throw new SenderException("IOException while Sending file ", ioe);
		} catch (SenderException re) {
			throw re;
		} catch (Exception e) {
			throw new SenderException("Unexpected Exception while Sending file ", e);
		} finally {
			try {
				socketChannel.close();
			} catch (IOException ioe) {
				// TODO Auto-generated catch block
				throw new SenderException("IOException while Closing socketChannel ", ioe);
			}
		}

	}

	private SocketChannel CreateChannel() {
		SocketChannel socketChannel;
		try {
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(true);

			SocketAddress sockAddr = new InetSocketAddress("localhost", port);
			socketChannel.connect(sockAddr);
			log.info("connection established .. {}", socketChannel.getRemoteAddress());

		} catch (IOException ioe) {
			throw new SenderException("IOException while Creating channel.Receiver is down. ", ioe);
		} catch (Exception e) {
			throw new SenderException("Unexpected Exception while Creating channel ", e);
		}
		return socketChannel;

	}

}
