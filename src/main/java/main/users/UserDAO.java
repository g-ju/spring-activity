package main.users;

import main.activity.Activity;

import java.util.Set;

public record UserDAO(long id, String username, String role, Set<Activity> activities)
{
}
