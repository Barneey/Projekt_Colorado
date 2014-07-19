package testClient;

import static org.junit.Assert.*;

import org.junit.Test;

import server_client.User;
import client.RequestNicknameFrame;

public class TestRequestNicknameFrame {

	@Test
	public void test() {
		char a[] = {'b'};
		new RequestNicknameFrame(new User("a", a));
	}
}