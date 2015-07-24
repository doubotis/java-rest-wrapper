/* 
 * Copyright (C) 2015 Christophe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.doubotis.restwrapper.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author Christophe
 */
public class APIPatternAnalyzer
{
    private static final String[] SUPPORTED_COMMANDS = { "GET", "POST", "DELETE" };
    
    private Vector<PatternDescriptor> mPatterns = new Vector<PatternDescriptor>();
    
    public APIPatternAnalyzer(String path)
    {
        try
        {
            File f = new File(path);
            FileInputStream fis = new FileInputStream(f);
            Scanner scanner = new Scanner(fis);
            while (scanner.hasNextLine())
            {
                String command = scanner.nextLine();
                
                // Bypass commments and results
                if (command.startsWith("//") || command.equals(""))
                    continue;
                
                // Now we are sure this is a true pattern descriptor, so handle it.
                PatternDescriptor pd = new PatternDescriptor();
                
                String[] sep = command.split(" ");
                int lastIndex = 0;
                for (int i=0; i < sep.length; i++)
                {
                    lastIndex = i;
                    if (Arrays.asList(SUPPORTED_COMMANDS).contains(sep[i]))
                    {
                        pd.mCommands.add(sep[i].toUpperCase());
                        continue;
                    }
                    
                    // We arrive at the end of recognized commands, the rest is the identifier and the pattern.
                    pd.mClass = sep[i];
                    lastIndex++;
                    break;
                }
                
                // Build the pattern depending on last splitted results.
                String pattern = "";
                for (int i=lastIndex; i < sep.length; i++)
                    pattern += sep[i];
                
                // Compile the pattern and store it into the descriptor.
                try
                {
                    pd.mPattern = Pattern.compile(pattern);
                    
                } catch (PatternSyntaxException pse)
                {
                    // This is not a severe error, we can bypass this descriptor and continue.
                    pse.printStackTrace();
                    continue;
                }
                
                // Pattern descriptor finished, store the descriptor into the vector.
                mPatterns.add(pd);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public List<PatternDescriptor> getPatterns() { return mPatterns; }
    public PatternDescriptor getPattern(int i) { return mPatterns.get(i); }
    public int getPatternSize() { return mPatterns.size(); }
    
    public PatternDescriptor find(String pattern)
    {
        for (PatternDescriptor pd : mPatterns)
        {
            Matcher matcher = pd.mPattern.matcher(pattern);
            if (matcher.matches())
                return pd;
        }
        return null;
    }
    
    public class PatternDescriptor
    {
        private Pattern mPattern;
        private Vector<String> mCommands = new Vector<String>();
        private String mClass;
        
        public Pattern getPattern() { return mPattern; }
        public List<String> getMethods() { return mCommands; }
        public String getImplementationClass() { return mClass; }
    }
    
    
}
