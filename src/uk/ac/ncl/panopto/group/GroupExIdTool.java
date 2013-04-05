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
package uk.ac.ncl.panopto.group;

import com.panopto.user.UserManagementStub;
import com.panopto.session.SessionManagementStub.ArrayOfguid;
import com.panopto.session.SessionManagementStub.AuthenticationInfo;
import com.panopto.session.SessionManagementStub.ArrayOfstring;
import com.panopto.session.SessionManagementStub.Folder;
import com.panopto.session.SessionManagementStub.FolderSortField;
import com.panopto.session.SessionManagementStub.GetFoldersList;
import com.panopto.session.SessionManagementStub.GetFoldersByExternalId;
import com.panopto.session.SessionManagementStub.GetFoldersById;
import com.panopto.session.SessionManagementStub.Guid;
import com.panopto.session.SessionManagementStub.ListFoldersRequest;
import com.panopto.session.SessionManagementStub.Pagination;
import com.panopto.session.SessionManagementStub.UpdateFolderExternalId;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import uk.ac.ncl.panopto.interfaces.ObjectWithExternalId;

/**
 * 
 * THIS CODE IS NOT TESTED, UNCOMMENT AT YOUR OWN RISK
 * 
 */
//public class GroupExIdTool implements ObjectWithExternalId
//{
//    private AuthenticationInfo auth;
//    private UserManagementStub stub;
//
//    public GroupExIdTool(String server, String user, String password)
//    {
//        this.auth = new AuthenticationInfo();
//        this.auth.setUserKey(user);
//        this.auth.setPassword(password);
//        try
//        {
//            stub = new UserManagementStub();
//        }
//        catch(org.apache.axis2.AxisFault af)
//        {
//            System.out.println(af.getMessage());
//        }
//    }
//   
//    @Override
//    public void getPanoptoObjectsByExternalId(String externalId)
//    {
//        if(externalId!=null&&!externalId.equalsIgnoreCase(""))
//        {
//            GetFoldersByExternalId gfbyei = new GetFoldersByExternalId();
//            gfbyei.setAuth(this.auth);
//            ArrayOfstring aos = new ArrayOfstring();
//            aos.setString(new String[]{externalId});
//            gfbyei.setFolderExternalIds(aos);
//            List<Folder> fl = null;
//            try
//            {
//                 fl = Arrays.asList(this.stub.get(gfbyei).getGetFoldersByExternalIdResult().getFolder());
//            }
//            catch(RemoteException re)
//            {
//                System.out.println(re.getMessage());
//            }
//            Iterator<Folder> fi = fl.iterator();
//            System.out.println("Found: ");
//            while(fi.hasNext())
//            {
//                this.printObject(fi.next());
//            }
//        }
//        else
//        {
//            System.out.println("An external id must have been set to search on it");
//        }
//    }
//
//    @Override
//    public Object getPanoptoObjectByName(String name)
//    {
//        Folder f = null;
//        Pagination p = new Pagination();
//        p.setMaxNumberResults(10);
//        ListFoldersRequest lfr = new ListFoldersRequest();
//        lfr.setPagination(p);
//        lfr.setParentFolderId(null);
//        lfr.setPublicOnly(false);
//        lfr.setSortBy(FolderSortField.Name);
//        lfr.setSortIncreasing(true);
//        GetFoldersList gfl = new GetFoldersList();
//        gfl.setAuth(this.auth);
//        gfl.setRequest(lfr);
//        gfl.setSearchQuery(null);
//        List<Folder> fl = null;
//        try
//        {
//             fl = Arrays.asList(this.stub.getFoldersList(gfl).getGetFoldersListResult().getResults().getFolder());
//        }
//        catch(RemoteException re)
//        {
//            System.out.println(re.getMessage());
//        }
//        Iterator<Folder> fi = fl.iterator();
//        while(fi.hasNext())
//        {
//            Folder fTemp = fi.next();
//            if(fTemp.getName().equalsIgnoreCase(name))
//            {
//                f = fTemp;
//                System.out.println("Found:");
//                this.printObject(f);
//            }
//        }
//        return f;
//    }
//
//    @Override
//    public Object getPanoptoObjectById(String id)
//    {
//        Folder f = null;
//        GetFoldersById gfbid = new GetFoldersById();
//        gfbid.setAuth(this.auth);
//        ArrayOfguid aog = new ArrayOfguid();
//        Guid[] g = new Guid[]{new Guid()};
//        g[0].setGuid(id);
//        aog.setGuid(g);
//        gfbid.setFolderIds(aog);
//        List<Folder> fl = null;
//        try
//        {
//            fl = Arrays.asList(this.stub.getFoldersById(gfbid).getGetFoldersByIdResult().getFolder());
//        }
//        catch(RemoteException re)
//        {
//            System.out.println(re.getMessage());
//        }
//        Iterator<Folder> fi = fl.iterator();
//        while(fi.hasNext())
//        {
//            Folder fTemp = fi.next();
//            if(fTemp.getId().getGuid().equalsIgnoreCase(id))
//            {
//                f = fTemp;
//                System.out.println("Found:");
//                this.printObject(f);
//            }
//        }
//        return f;
//    }
//
//    @Override
//    public void printObject(Object o)
//    {
//        Folder f = (Folder)o;
//        System.out.println("Name: "+f.getName());
//        System.out.println("Id: "+f.getId());
//        System.out.println("ExId: "+f.getExternalId());
//    }
//
//    @Override
//    public void updateExternalIdById(String id, String externalId)
//    {
//        Folder f = (Folder)this.getPanoptoObjectById(id);
//        this.updateExternalId(f, externalId);
//    }
//
//    @Override
//    public void updateExternalIdByName(String name, String externalId)
//    {
//        Folder f = (Folder)this.getPanoptoObjectByName(name);
//        this.updateExternalId(f, externalId);
//    }
//
//    private void updateExternalId(Folder f, String externalId)
//    {
//        if(f!=null)
//        {
//            UpdateFolderExternalId ufei = new UpdateFolderExternalId();
//            ufei.setAuth(this.auth);
//            ufei.setExternalId(externalId);
//            ufei.setFolderId(f.getId());
//            try
//            {
//                this.stub.updateFolderExternalId(ufei);
//            }
//            catch(RemoteException re)
//            {
//                System.out.println(re.getMessage());
//                return;
//            }
//            System.out.println("Folder "+f.getName()+" updated with external Id "+externalId);
//        }
//        else
//        {
//            System.out.println("\nFolder not found");
//        }
//    }
//}
