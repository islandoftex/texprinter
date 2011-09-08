/**
 * \cond LICENSE
 * ********************************************************************
 * This is a conditional block for preventing the DoxyGen documentation
 * tool to include this license header within the description of each
 * source code file. If you want to include this block, please define
 * the LICENSE parameter into the provided DoxyFile.
 * ********************************************************************
 *
 * TeXPrinter - A TeX.SX question printer
 * Copyright (c) 2011, Paulo Roberto Massa Cereda
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. Neither the name of the project's author nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ********************************************************************
 * End of the LICENSE conditional block
 * ********************************************************************
 * \endcond
 *
 * <b>PostComparator.java</b>: This class implements a comparator for lists,
 * based on votes and acceptance marks.
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import java.util.Comparator;
import net.sf.texprinter.model.Post;

/**
 * Implements a comparator for lists based on votes and acceptance marks.
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.1
 */
public class PostComparator implements Comparator<Post> {

    /**
     * Compares two objects and return the priority.
     * @param o1 Object one.
     * @param o2 Object two.
     * @return The priority.
     */
    @Override
    public int compare(Post o1, Post o2) {
        
        // if both are accepted
        if (o1.isAccepted() && o2.isAccepted()) {
            
            // the highest score comes first
            return ((o1.getVotes() > o2.getVotes() ? +1 : (o1.getVotes() < o2.getVotes() ? -1 : 0)) * -1);
        }
        else {
            
            // only the first one is accepted
            if (o1.isAccepted()) {
                
                // it comes first
                return -1;
            }
            else {
                
                // only the second one is accepted
                if (o2.isAccepted()) {
                    
                    // it comes first
                    return +1;
                }
                else {
                    
                    // the highest score comes first
                    return ((o1.getVotes() > o2.getVotes() ? +1 : (o1.getVotes() < o2.getVotes() ? -1 : 0)) * -1);
                }
            }
        }
    }
    
}
