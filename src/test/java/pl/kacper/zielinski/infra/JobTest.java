package pl.kacper.zielinski.infra;

import org.junit.jupiter.api.Test;

import java.io.File;

public class JobTest {

    private Job job;

    @Test
    private void shouldCreateFolderStructureIfNotExists() {
        // given
        // clear DEV, TEST, HOME folders

        // when
        job.initCatalogStructure();

        // then
        // structure should exists
    }

    @Test
    private void shouldMoveJarFileToDev() {
        // given
        File jarFile = new File("test.jar");
        // change hour to

        // when
        job.moveFilesFromHomeDirectory();

        // then

    }

    @Test
    private void shouldMoveJarFileToTest() {

    }

    @Test
    private void shouldMoveXmlFileToDev() {

    }


}
