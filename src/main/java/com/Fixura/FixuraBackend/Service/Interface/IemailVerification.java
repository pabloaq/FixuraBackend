package com.Fixura.FixuraBackend.Service.Interface;

public interface IemailVerification {
  void sendEmailVerification(String email, String token);
}
