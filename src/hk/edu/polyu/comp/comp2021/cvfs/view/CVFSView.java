package hk.edu.polyu.comp.comp2021.cvfs.view;

import hk.edu.polyu.comp.comp2021.cvfs.model.Directory;

/**
 * the view elements of the CVFS
 */
public class CVFSView {
    /**
     * The string storing the current path of the working directory.
     */
    private String curDirPath;


    /**
     * Print the welcome message.
     */
    public void welcome() {
        System.out.println("\n" +
                "██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗    ████████╗ ██████╗      ██████╗██╗   ██╗███████╗███████╗\n" +
                "██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝    ╚══██╔══╝██╔═══██╗    ██╔════╝██║   ██║██╔════╝██╔════╝\n" +
                "██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗         ██║   ██║   ██║    ██║     ██║   ██║█████╗  ███████╗\n" +
                "██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝         ██║   ██║   ██║    ██║     ╚██╗ ██╔╝██╔══╝  ╚════██║\n" +
                "╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗       ██║   ╚██████╔╝    ╚██████╗ ╚████╔╝ ██║     ███████║\n" +
                " ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝       ╚═╝    ╚═════╝      ╚═════╝  ╚═══╝  ╚═╝     ╚══════╝\n" +
                "                                                                                                                         \n" +
                "                                                     Developed by Group 30: MAN Furui, WANG Meng, XING Shiji, ZHANG Yubo.\n" +
                "                        COMP2021 Object-Oriented Programming, Year 2020 Semester 1, The Hong Kong Polytechnic University.\n"
        );
    }

    /**
     * Update the path of current working directory after changing.
     *
     * @param cur The current working directory.
     */
    public void updateDir(Directory cur) {
        if (cur == null) {
            curDirPath = "> ";
            return;
        }
        StringBuilder str = cur.getPath();
        str.append("> ");
        curDirPath = str.toString();
    }


    /**
     * Print a prompt including the current working directory.
     */
    public void printPrompt() {
        System.out.print(curDirPath);
    }
}
