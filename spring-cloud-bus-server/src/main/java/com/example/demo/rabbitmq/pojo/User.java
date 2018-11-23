package com.example.demo.rabbitmq.pojo;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = -6122258626009806485L;
		private String name;
        private String pass;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getPass() {
            return pass;
        }
        public void setPass(String pass) {
            this.pass = pass;
        }
}