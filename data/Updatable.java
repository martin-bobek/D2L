package data;

import java.io.Serializable;

import serverMessage.Request;

public interface Updatable extends Serializable {
	Request createRequest();
}
