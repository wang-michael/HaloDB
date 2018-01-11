package amannaly;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Arjun Mannaly
 */
public class DBMetaDataTest {

    private static final String directory = "/tmp/DBMetaDataTest/testDBMetaData";

    @Test
    public void testDBMetaData() throws IOException {
        Path metaDataFile = Paths.get(directory, DBMetaData.METADATA_FILE_NAME);

        // confirm that the file doesn't exist.
        Assert.assertFalse(Files.exists(metaDataFile));

        DBMetaData metaData = new DBMetaData(directory);
        metaData.loadFromFile();

        // file has not yet been created, return default values.
        Assert.assertFalse(metaData.isOpen());
        Assert.assertEquals(metaData.getSequenceNumber(), 0);

        metaData.setOpen(true);
        metaData.setSequenceNumber(100);
        metaData.storeToFile();

        // confirm that the file has been created.
        Assert.assertTrue(Files.exists(metaDataFile));

        // load again to read stored values.
        metaData = new DBMetaData(directory);
        metaData.loadFromFile();

        Assert.assertTrue(metaData.isOpen());
        Assert.assertEquals(metaData.getSequenceNumber(), 100);

        metaData.setOpen(false);
        metaData.setSequenceNumber(Long.MAX_VALUE);
        metaData.storeToFile();

        // load again to read stored values.
        metaData = new DBMetaData(directory);
        metaData.loadFromFile();

        Assert.assertFalse(metaData.isOpen());
        Assert.assertEquals(metaData.getSequenceNumber(), Long.MAX_VALUE);
    }

    @BeforeMethod
    public void createDirectory() throws IOException {
        FileUtils.createDirectoryIfNotExists(new File(directory));
    }

    @AfterMethod
    public void deleteDirectory() throws IOException {
        TestUtils.deleteDirectory(new File(directory));
    }

}
