package main.users;

class UserNotFoundException extends RuntimeException
{
    UserNotFoundException(Long id)
    {
        super("Could not find User " + id);
    }
}
