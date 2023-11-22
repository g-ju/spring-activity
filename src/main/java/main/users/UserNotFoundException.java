package main.users;

class UserNotFoundException extends RuntimeException
{
    UserNotFoundException(Long id)
    {
        super("Cound not find User " + id);
    }
}
