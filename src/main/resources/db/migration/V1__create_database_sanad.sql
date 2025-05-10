-- This migration ensures the 'sanad' database exists. If not, it creates it.

-- Check if the 'sanad' database exists, if not, create it.
DO
$$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'sanad') THEN
        PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE sanad');
    END IF;
END
$$;
