/**
 * \cond LICENSE
 * ********************************************************************
 * This is a conditional block for preventing the DoxyGen documentation
 * tool to include this license header within the description of each
 * source code file. If you want to include this block, please define
 * the LICENSE parameter into the provided DoxyFile.
 * ********************************************************************
 *
 * TeXPrinter - A TeX.SX question printer Copyright (c) 2012, Paulo Roberto
 * Massa Cereda All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the project's author nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * ********************************************************************
 * End of the LICENSE conditional block
 * ********************************************************************
 * \endcond
 *
 * Dialogs.java: This class provides message features to the other classes.
 * Last revision: paulo at temperantia 26 Feb 2012 05:57
 * 
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import javax.swing.SwingUtilities;
import net.sf.texprinter.ui.*;

/**
 * Provides message features to the other classes.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 1.0
 */
public class Dialogs {
    
    private String strReturn = "";
    private PrintingFormat prtReturn = PrintingFormat.NONE;
    
    //private Object result;

    // define the output controller
    private static OutputController outControl = OutputController.getInstance();

    public PrintingFormat getOutputFormat() {

        // create a new window
        //PrintingWindow pw = new PrintingWindow();
        
        // return the result of the dialog
        //return pw.show();
        
        try {
        
        

        SwingUtilities.invokeAndWait(new Runnable() {
            
                @Override
            public void run() {
                //SimpleDialog sd = new SimpleDialog();
                //sd.setVisible(true);
                //PrintingWindow pw = new PrintingWindow();
                //prtReturn = pw.show();
                    
                    NewPrintingWindow npw = new NewPrintingWindow();
                    prtReturn = npw.show();
                    //result = (PrintingFormat) npw.show();

            }
        });
        
            //System.out.println("Retorno:? " + prtReturn.toString());
        
        return prtReturn;
        //return ((PrintingFormat) result);
        
        }
        catch (Exception e) {
            return null;
        }
        
    }

    /**
     * Displays the showExceptionWindow.
     */
    public static void showExceptionWindow() {

        // if it is GUI mode
        if (!outControl.isCommandLineMode()) {

            // create a new window
            //ExceptionWindow ew = new ExceptionWindow();
            
            // show
            //ew.show();

            // exit application
            //System.exit(0);
            
            try {
        
        

        SwingUtilities.invokeAndWait(new Runnable() {
            
                @Override
            public void run() {
                //SimpleDialog sd = new SimpleDialog();
                //sd.setVisible(true);
                //MainWindow mw = new MainWindow();
                //strReturn = mw.show();
                    ExceptionWindow ew = new ExceptionWindow();
                    ew.show();
                    System.exit(0);
            }
        });

        
        }
        catch (Exception e) {
        }

        } else {

            // Display the showExceptionWindow
            System.out.println("Unfortunately, TeXPrinter raised an exception. It might be a bug, or\nsimply a temporary technical dificulty.");

            // exit the application
            System.exit(0);
        }

    }

    /**
     * Show the about messagebox.
     */
    public static void showAboutWindow() {

        // create a new window
        AboutWindow aw = new AboutWindow();
        
        // show
        aw.show();

//        try {
//        
//        
//
//        SwingUtilities.invokeAndWait(new Runnable() {
//            
//                @Override
//            public void run() {
//                // create a new window
//        AboutWindow aw = new AboutWindow();
//        
//        // show
//        aw.show();
//                    
//                    
//            }
//        });
//
//        
//        }
//        catch (Exception e) {
//        }
        
    }

    /**
     * Show the changelog window.
     */
    public static void showChangelogWindow() {

        // create a new window
        ChangelogWindow cw = new ChangelogWindow();
        
        // show
        cw.show();
        
//        try {
//        
//        
//
//        SwingUtilities.invokeAndWait(new Runnable() {
//            
//                @Override
//            public void run() {
//                // create a new window
//        ChangelogWindow cw = new ChangelogWindow();
//        
//        // show
//        cw.show();
//                    
//                    
//            }
//        });
//
//        
//        }
//        catch (Exception e) {
//        }

    }

    /**
     * Show the input dialog.
     *
     * @return The textfield value.
     */
    public String getQuestionID() {
        

        
        try {
        
        

        SwingUtilities.invokeAndWait(new Runnable() {
            
                @Override
            public void run() {
                //SimpleDialog sd = new SimpleDialog();
                //sd.setVisible(true);
                MainWindow mw = new MainWindow();
                strReturn = mw.show();
                //result = (String) mw.show();
            }
        });
        
        return strReturn;
        //return ((String) result);
        
        }
        catch (Exception e) {
            return null;
        }
        
        // create a new window
        //MainWindow mw = new MainWindow();
        
        // return the value
        //return mw.show();

    }

    /**
     * Show the 404 error window.
     */
    public static void showNotFoundWindow() {

        // create a new window.
        //NotFoundWindow nfw = new NotFoundWindow();
        
        // show
        //nfw.show();
        
        try {
        
        

        SwingUtilities.invokeAndWait(new Runnable() {
            
                @Override
            public void run() {
                //SimpleDialog sd = new SimpleDialog();
                //sd.setVisible(true);
                //MainWindow mw = new MainWindow();
                //strReturn = mw.show();
                    //ExceptionWindow ew = new ExceptionWindow();
                    //ew.show();
                    //System.exit(0);
                    NotFoundWindow nfw = new NotFoundWindow();
                    nfw.show();
                    
                    
            }
        });

        
        }
        catch (Exception e) {
        }

    }

    /**
     * Show a funny message when one of the steps is
     * ignored.
     */
    public static void showKittenWindow() {

        // create a new window.
        //KittenWindow kw = new KittenWindow();
        
        // show
        //kw.show();
        
        try {
        
        

        SwingUtilities.invokeAndWait(new Runnable() {
            
                @Override
            public void run() {
                //SimpleDialog sd = new SimpleDialog();
                //sd.setVisible(true);
                //MainWindow mw = new MainWindow();
                //strReturn = mw.show();
                    //ExceptionWindow ew = new ExceptionWindow();
                    //ew.show();
                    //System.exit(0);
                    // create a new window.
        KittenWindow kw = new KittenWindow();
        
        // show
        kw.show();
                    
                    
            }
        });

        
        }
        catch (Exception e) {
        }
        
        
    }

    /**
     * Show a confirmation window when finished.
     */
    public static void showConfirmationWindow() {

        // create a new window.
        //ConfirmationWindow cw = new ConfirmationWindow();
        
        // show
        //cw.show();
        
        try {
        
        

        SwingUtilities.invokeAndWait(new Runnable() {
            
                @Override
            public void run() {
                //SimpleDialog sd = new SimpleDialog();
                //sd.setVisible(true);
                //MainWindow mw = new MainWindow();
                //strReturn = mw.show();
                    //ExceptionWindow ew = new ExceptionWindow();
                    //ew.show();
                    //System.exit(0);
          // create a new window.
        ConfirmationWindow cw = new ConfirmationWindow();
        
        // show
        cw.show();
                    
                    
            }
        });

        
        }
        catch (Exception e) {
        }
    }

    /**
     * Show help for formats.
     */
    public static void showHelpFormatWindow() {
        
        // create a new window.
        HelpFormatWindow hfw = new HelpFormatWindow();
        
        // show
        hfw.show();
        
//        try {
//        
//        
//
//        SwingUtilities.invokeAndWait(new Runnable() {
//            
//                @Override
//            public void run() {
//                //SimpleDialog sd = new SimpleDialog();
//                //sd.setVisible(true);
//                //MainWindow mw = new MainWindow();
//                //strReturn = mw.show();
//                    //ExceptionWindow ew = new ExceptionWindow();
//                    //ew.show();
//                    //System.exit(0);
//          // create a new window.
//        HelpFormatWindow hfw = new HelpFormatWindow();
//        
//        // show
//        hfw.show();
//                    
//                    
//            }
//        });
//
//        
//        }
//        catch (Exception e) {
//        }
    }

    /**
     * Show help for error report.
     */
    public static void showHelpErrorReportWindow() {
        
        // create a new window.
        HelpErrorReportWindow herw = new HelpErrorReportWindow();
        
        // show
        herw.show();
        
//        try {
//        
//        
//
//        SwingUtilities.invokeAndWait(new Runnable() {
//            
//                @Override
//            public void run() {
//                // create a new window.
//        HelpErrorReportWindow herw = new HelpErrorReportWindow();
//        
//        // show
//        herw.show();
//                    
//                    
//            }
//        });
//
//        
//        }
//        catch (Exception e) {
//        }
    }
}
