package com.tulin.v8.flowdesigner.ui.editors.process.element;

import org.json.JSONArray;

public class ConnectLine implements IElement {
	private String id;
	private String name;
	private String type;
	private String shape;
	private String number;
	private String from;
	private String to;
	private String fromx;
	private String fromy;
	private String tox;
	private String toy;
	private String polydot;
	private JSONArray property;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFromx() {
		return fromx;
	}

	public void setFromx(String fromx) {
		this.fromx = fromx;
	}

	public String getFromy() {
		return fromy;
	}

	public void setFromy(String fromy) {
		this.fromy = fromy;
	}

	public String getTox() {
		return tox;
	}

	public void setTox(String tox) {
		this.tox = tox;
	}

	public String getToy() {
		return toy;
	}

	public void setToy(String toy) {
		this.toy = toy;
	}

	public String getPolydot() {
		return polydot;
	}

	public void setPolydot(String polydot) {
		this.polydot = polydot;
	}

	public JSONArray getProperty() {
		return property;
	}

	public void setProperty(JSONArray property) {
		this.property = property;
	}

}
