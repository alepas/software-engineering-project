package server.model.users;

import shared.exceptions.usersAndDatabaseExceptions.DatabaseFileErrorException;

import java.io.*;



class LoadingFromFile {

    private LoadingFromFile() {
    }

    static Object fromFile(String name) throws DatabaseFileErrorException {
        Object output;

        try (InputStream fis = new FileInputStream(name);
                ObjectInputStream ois=new ObjectInputStream(fis);
                ) {
            output = ois.readObject();

        } catch (FileNotFoundException e) {
            throw new DatabaseFileErrorException(1);
        } catch (IOException | ClassNotFoundException e) {
            throw new DatabaseFileErrorException(0);
        }


        return output;
    }

    static void toFile(Object root, String name) throws DatabaseFileErrorException {


        try (
                FileOutputStream fos = new FileOutputStream(name);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        )
        {
            oos.writeObject(root);
        }

        catch (FileNotFoundException e) {
            throw new DatabaseFileErrorException(1);
        }
        catch (IOException e) {
            throw new DatabaseFileErrorException(0);}

    }


}