package com.api.project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CredenciaisDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private String login;
	private String password;
	
}
