package com.akura.integration.utility;

/**
 * Class that represents a File manager.
 */
public class FileResourceManager {


    // TODO: 4/26/17 Add any other file related function under this class.

    /**
     * Function used to get the path of the file from resource folder.
     *
     * @param fileName - File which is located in the resources folder E.g: <customFolderName>/<fileName>
     * @return - the complete path of the file.
     */
    public String getFilePath(String fileName) {
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();

        return classLoader.getResource(fileName).getPath();
    }
}
