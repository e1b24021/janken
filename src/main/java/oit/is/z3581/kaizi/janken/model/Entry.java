package oit.is.z3581.kaizi.janken.model;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

@Component
@Scope("singleton")
public class Entry {
  private final Set<String> users = new ConcurrentSkipListSet<>();

  public void addUser(String username) {
    users.add(username);
  }

  public Set<String> getUsers() {
    return users;
  }
}
