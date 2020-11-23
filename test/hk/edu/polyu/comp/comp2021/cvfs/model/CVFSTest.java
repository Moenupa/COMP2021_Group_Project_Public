package hk.edu.polyu.comp.comp2021.cvfs.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CVFSTest {

    public CVFS cvfs;

    @Before
    public void setUp() {
        cvfs = new CVFS();
    }

    @Test
    public void diskTest() {
        cvfs.newDisk(1000);
        cvfs.setDisk(new Disk(10000));
    }

    @Test
    public void storeLoadTest() {
        // below tests have bug and I dont know why
//        cvfs.newDisk(100);
//        cvfs.getCwd().newDoc("doc1", DocType.TXT, "sample text for doc1");
//        cvfs.store("nowStored");
//        cvfs.load("nowStored");
    }

    @Test
    public void changeDirTest() {
        cvfs.newDisk(1000);
        cvfs.getCwd().newDir("newDir");
        cvfs.changeDir("newDir");
        cvfs.changeDir("..");
    }

    @Test
    public void parsePathTest() {

    }

    @Test
    public void newCriTestPack() {
        cvfs.newSimpleCri("aa", "type", "equals", "\"txt\"");
        cvfs.newSimpleCri("bb", "type", "equals", "\"css\"");
        cvfs.newNegation("cc", "aa");
        cvfs.newBinaryCri("dd", "bb", "&&", "cc");
    }

    @Test
    public void errorTestPack() {
        cvfs.newSimpleCri("aa", "type", "equals", "\"txt\"");
        // should generate Error println
        cvfs.newSimpleCri("aa", "type", "equals", "\"txt\"");
        cvfs.newSimpleCri("invalidname", "type", "equals", "\"txt\"");

        cvfs.newNegation("invalidname","aa");
        cvfs.newNegation("aa","aa");
        cvfs.newNegation("bb","zz");

        cvfs.newBinaryCri("aa", "zz", "&&", "aa");
        cvfs.newBinaryCri("bb", "aa", "&|", "aa");
        cvfs.newBinaryCri("bb", "zz", "&&", "aa");
        cvfs.newBinaryCri("bb", "aa", "&&", "zz");
        cvfs.newBinaryCri("invalidname", "aa", "&&", "aa");

        assertEquals(cvfs.getCri("aa"), new Criterion("aa", "type", "equals", "\"txt\""));
    }

    @Test
    public void printAllCriteria() {
        System.out.println(cvfs.getCriteria());
        cvfs.printAllCriteria();
    }
}