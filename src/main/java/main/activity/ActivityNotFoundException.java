package main.activity;

class ActivityNotFoundException extends RuntimeException
{
    ActivityNotFoundException(Long id)
    {
        super("Could not find Activity " + id);
    }
}
