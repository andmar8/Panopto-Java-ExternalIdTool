    /*
     * This file is part of Panopto-Java-ExternalIdTool.
     * 
     * Panopto-Java-ExternalIdTool is free software: you can redistribute it and/or modify
     * it under the terms of the GNU General Public License as published by
     * the Free Software Foundation, either version 3 of the License, or
     * (at your option) any later version.
     * 
     * Panopto-Java-ExternalIdTool is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     * GNU General Public License for more details.
     * 
     * You should have received a copy of the GNU General Public License
     * along with Panopto-Java-ExternalIdTool.  If not, see <http://www.gnu.org/licenses/>.
     * 
     * Copyright: Andrew Martin, Newcastle University
     * 
     */
package uk.ac.ncl.panopto.externalIdTool;

import uk.ac.ncl.panopto.interfaces.ObjectWithExternalId;
import uk.ac.ncl.panopto.remoteRecorder.RemoteRecorderExIdTool;
import uk.ac.ncl.panopto.folder.FolderExIdTool;
import uk.ac.ncl.panopto.session.SessionExIdTool;

public class ExIdTool
{
    public static void main(String[] args)
    {
        //get   folder|recorder|session   name|id|exid  <name|id|exid>
        //setExId   folder|recorder|session   name|id       <name|id>        <exid>
       
        if(args.length>=7&&args.length<=8)
        {
            ObjectWithExternalId owei = getExIdTool(args[0],args[1],args[2],args[4]);
            if(owei!=null)
            {
                if(args[3].equalsIgnoreCase("get")&&args.length==7)
                {
                    if(args[5].equalsIgnoreCase("exid"))
                    {
                        owei.getPanoptoObjectsByExternalId(args[6]);
                        return;
                    }
                    else if(args[5].equalsIgnoreCase("id"))
                    {
                        owei.getPanoptoObjectById(args[6]);
                        return;
                    }
                    else if(args[5].equalsIgnoreCase("name"))
                    {
                        owei.getPanoptoObjectByName(args[6]);
                        return;
                    }
                }
                else if(args[3].equalsIgnoreCase("setExId")&&args.length==8)
                {
                    if(args[5].equalsIgnoreCase("id"))
                    {
                        owei.updateExternalIdById(args[6],args[7]);
                    return;
                    }
                    else if(args[5].equalsIgnoreCase("name"))
                    {
                        owei.updateExternalIdByName(args[6],args[7]);
                        return;
                    }
                }
            }
        }
        System.out.println("Usage: java -jar PanoptoExternalIdTool <Server> <Username> <Password> <Operation> <Type> <By> <Name|Id|ExternalId> [Desired ExternalId(set only)]"
                + "\nE.g.:"
                + "\n\t\tget\tfolder|recorder|session\tname|id|exid\t<name|id|exid>"
                + "\n\t\tsetExId\tfolder|recorder|session\tname|id\t\t<name|id>\t<exid>"
                + "\n\n\tjava -jar PanoptoExternalIdTool panoptowfe.ncl.ac.uk admin password setExId folder name MAS1342 Q1213-MAS1342"
                + "\n\tjava -jar PanoptoExternalIdTool panoptowfe.ncl.ac.uk admin password get folder name MAS1342"
                + "\n\tjava -jar PanoptoExternalIdTool panoptowfe.ncl.ac.uk admin password get folder exid Q1213-MAS1342");
    }
    
    private static ObjectWithExternalId getExIdTool(String server, String user, String password, String type)
    {
        if(type.equalsIgnoreCase("folder")){return new FolderExIdTool(server,user,password);}
        else if(type.equalsIgnoreCase("recorder")){return new RemoteRecorderExIdTool(server,user,password);}
        else if(type.equalsIgnoreCase("session")){return new SessionExIdTool(server,user,password);}
        return null;
    }
}
