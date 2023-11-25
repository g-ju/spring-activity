package main.activity;

public class ActivityNotFoundException extends RuntimeException
{
    public ActivityNotFoundException(Long id)
    {
        super("Could not find Activity " + id);
    }
}
