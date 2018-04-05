package data;

import java.io.Serializable;

import message.Request;

public interface Updatable extends Serializable {
	Request createRequest();
}
