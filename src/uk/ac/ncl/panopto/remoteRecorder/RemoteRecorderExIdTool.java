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
package uk.ac.ncl.panopto.remoteRecorder;

import com.panopto.remoterecorder.RemoteRecorderManagementStub;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.ArrayOfguid;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.ArrayOfstring;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.AuthenticationInfo;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.GetRemoteRecordersByExternalId;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.GetRemoteRecordersById;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.Guid;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.ListRecorders;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.Pagination;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.RecorderSortField;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.RemoteRecorder;
import com.panopto.remoterecorder.RemoteRecorderManagementStub.UpdateRemoteRecorderExternalId;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import uk.ac.ncl.panopto.interfaces.ObjectWithExternalId;

public class RemoteRecorderExIdTool implements ObjectWithExternalId
{
    private AuthenticationInfo auth;
    private RemoteRecorderManagementStub stub;
        
    public RemoteRecorderExIdTool(String server, String user, String password)
    {
        this.auth = new AuthenticationInfo();
        this.auth.setUserKey(user);
        this.auth.setPassword(password);
        try
        {
            stub = new RemoteRecorderManagementStub("https://"+server+"/Panopto/PublicAPI/4.2/RemoteRecorderManagement.svc?wsdl");
        }
        catch(org.apache.axis2.AxisFault af)
        {
            System.out.println(af.getMessage());
        }
    }

    @Override
    public void getPanoptoObjectsByExternalId(String externalId)
    {
        if(externalId!=null&&!externalId.equalsIgnoreCase(""))
        {
            GetRemoteRecordersByExternalId grrbyei = new GetRemoteRecordersByExternalId();
            grrbyei.setAuth(this.auth);
            ArrayOfstring aos = new ArrayOfstring();
            aos.setString(new String[]{externalId});
            grrbyei.setExternalIds(aos);
            List<RemoteRecorder> rrl = null;
            try
            {
                rrl = Arrays.asList(this.stub.getRemoteRecordersByExternalId(grrbyei).getGetRemoteRecordersByExternalIdResult().getRemoteRecorder());
            }
            catch(NullPointerException npe)
            {
                System.out.println("\nNo remote recorder(s) found");
                return;
            }
            catch(RemoteException re)
            {
                System.out.println(re.getMessage());
            }
            Iterator<RemoteRecorder> rri = rrl.iterator();
            while(rri.hasNext())
            {
                this.printObject(rri.next());
            }
        }
        else
        {
            System.out.println("An external id must have been set to search on it");
        }
    }

    @Override
    public Object getPanoptoObjectByName(String name)
    {
        RemoteRecorder rr = null;
        try
        {
            ListRecorders lr = new ListRecorders();
            lr.setAuth(this.auth);
            Pagination p = new Pagination();
            p.setMaxNumberResults(100);
            lr.setPagination(p);
            lr.setSortBy(RecorderSortField.Name);
            List<RemoteRecorder> rrl = Arrays.asList(stub.listRecorders(lr).getListRecordersResult().getPagedResults().getRemoteRecorder());
            Iterator<RemoteRecorder> rri = rrl.iterator();
            while(rri.hasNext())
            {
                RemoteRecorder rrTemp = rri.next();
                if(rrTemp.getName().equalsIgnoreCase(name))
                {
                    rr = rrTemp;
                    this.printObject(rrTemp);
                    break;
                }
            }
            if(rr==null)
            {
                System.out.println("\nNo remote recorder(s) found");
            }
        }
        catch(RemoteException re)
        {
            System.out.println(re.getMessage());
        }
        return rr;
    }

    @Override
    public Object getPanoptoObjectById(String id)
    {
        RemoteRecorder rr = null;
        GetRemoteRecordersById grrbid = new GetRemoteRecordersById();
        grrbid.setAuth(this.auth);
        ArrayOfguid aog = new ArrayOfguid();
        Guid[] g = new Guid[]{new Guid()};
        g[0].setGuid(id);
        aog.setGuid(g);
        grrbid.setRemoteRecorderIds(aog);
        List<RemoteRecorder> rrl = null;
        try
        {
             rrl = Arrays.asList(this.stub.getRemoteRecordersById(grrbid).getGetRemoteRecordersByIdResult().getRemoteRecorder());
        }
        catch(RemoteException re)
        {
            System.out.println(re.getMessage());
        }
        Iterator<RemoteRecorder> rri = rrl.iterator();
        while(rri.hasNext())
        {
            RemoteRecorder rrTemp = rri.next();
            if(rrTemp.getId().getGuid().equalsIgnoreCase(id))
            {
                rr = rrTemp;
                this.printObject(rrTemp);
            }
        }
        return rr;
    }

    @Override
    public void printObject(Object o)
    {
        RemoteRecorder rr = (RemoteRecorder)o;
        System.out.println("\nName: "+rr.getName());
        System.out.println("Id: "+rr.getId());
        System.out.println("ExId: "+rr.getExternalId());
    }

    @Override
    public void updateExternalIdById(String id, String externalId)
    {
        RemoteRecorder rr = (RemoteRecorder)this.getPanoptoObjectById(id);
        this.updateExternalId(rr, externalId);
    }

    @Override
    public void updateExternalIdByName(String name, String externalId)
    {
        RemoteRecorder rr = (RemoteRecorder)this.getPanoptoObjectByName(name);
        this.updateExternalId(rr, externalId);
    }
    
    private void updateExternalId(RemoteRecorder rr, String externalId)
    {
        if(rr!=null)
        {
            UpdateRemoteRecorderExternalId urrei = new UpdateRemoteRecorderExternalId();
            urrei.setAuth(this.auth);
            urrei.setExternalId(externalId);
            urrei.setRemoteRecorderId(rr.getId());
            try
            {
                stub.updateRemoteRecorderExternalId(urrei);
            }
            catch(RemoteException re)
            {
                System.out.println(re.getMessage());
                return;
            }
            System.out.println("\nRemote recorder "+rr.getName()+" updated with external Id "+externalId);
        }
        else
        {
            System.out.println("\nRemote recorder not found");
        }
    }
}
