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
package uk.ac.ncl.panopto.session;

import com.panopto.session.SessionManagementStub;
import com.panopto.session.SessionManagementStub.ArrayOfSession;
import com.panopto.session.SessionManagementStub.ArrayOfguid;
import com.panopto.session.SessionManagementStub.AuthenticationInfo;
import com.panopto.session.SessionManagementStub.ArrayOfstring;
import com.panopto.session.SessionManagementStub.SessionSortField;
import com.panopto.session.SessionManagementStub.GetSessionsList;
import com.panopto.session.SessionManagementStub.GetSessionsByExternalId;
import com.panopto.session.SessionManagementStub.GetSessionsByExternalIdResponse;
import com.panopto.session.SessionManagementStub.GetSessionsById;
import com.panopto.session.SessionManagementStub.Guid;
import com.panopto.session.SessionManagementStub.ListSessionsRequest;
import com.panopto.session.SessionManagementStub.Pagination;
import com.panopto.session.SessionManagementStub.Session;
import com.panopto.session.SessionManagementStub.UpdateSessionExternalId;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import uk.ac.ncl.panopto.interfaces.ObjectWithExternalId;

public class SessionExIdTool implements ObjectWithExternalId
{
    private AuthenticationInfo auth;
    private SessionManagementStub stub;

    public SessionExIdTool(String server, String user, String password)
    {
        this.auth = new AuthenticationInfo();
        this.auth.setUserKey(user);
        this.auth.setPassword(password);
        try
        {
            stub = new SessionManagementStub("https://"+server+"/Panopto/PublicAPI/4.2/SessionManagement.svc?wsdl");
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
            GetSessionsByExternalId gsbyei = new GetSessionsByExternalId();
            gsbyei.setAuth(this.auth);
            ArrayOfstring aos = new ArrayOfstring();
            aos.setString(new String[]{externalId});
            gsbyei.setSessionExternalIds(aos);
            List<Session> sl = null;
            try
            {
                GetSessionsByExternalIdResponse gsbyer = this.stub.getSessionsByExternalId(gsbyei);
                if(gsbyer.isGetSessionsByExternalIdResultSpecified())
                {
                    ArrayOfSession aoSess = gsbyer.getGetSessionsByExternalIdResult();
                    if(aoSess.isSessionSpecified())
                    {
                        Session[] sArray = aoSess.getSession();
                        sl = Arrays.asList(sArray);
                    }
                    else
                    {
                        System.out.println("\nNo sessions in response");
                        return;
                    }
                }
                else
                {
                    System.out.println("\nNo response to request");
                    return;
                }
                Iterator<Session> si = sl.iterator();
                while(si.hasNext())
                {
                    this.printObject(si.next());
                }
            }
            catch(RemoteException re)
            {
                System.out.println(re.getMessage());
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
        Session s = null;
        Pagination p = new Pagination();
        p.setMaxNumberResults(10);
        ListSessionsRequest lsr = new ListSessionsRequest();
        lsr.setPagination(p);
        lsr.setSortBy(SessionSortField.Name);
        lsr.setSortIncreasing(true);
        GetSessionsList gsl = new GetSessionsList();
        gsl.setAuth(this.auth);
        gsl.setRequest(lsr);
        gsl.setSearchQuery(name);
        List<Session> sl = null;
        try
        {
             sl = Arrays.asList(this.stub.getSessionsList(gsl).getGetSessionsListResult().getResults().getSession());
        }
        catch(RemoteException re)
        {
            System.out.println(re.getMessage());
        }
        Iterator<Session> si = sl.iterator();
        while(si.hasNext())
        {
            Session sTemp = si.next();
            if(sTemp.getName().equalsIgnoreCase(name))
            {
                s = sTemp;
                this.printObject(sTemp);
                break;
            }
        }
        return s;
    }

    @Override
    public Object getPanoptoObjectById(String id)
    {
        Session s = null;
        GetSessionsById gsbid = new GetSessionsById();
        gsbid.setAuth(this.auth);
        ArrayOfguid aog = new ArrayOfguid();
        Guid[] g = new Guid[]{new Guid()};
        g[0].setGuid(id);
        aog.setGuid(g);
        gsbid.setSessionIds(aog);
        List<Session> sl = null;
        try
        {
             sl = Arrays.asList(this.stub.getSessionsById(gsbid).getGetSessionsByIdResult().getSession());
        }
        catch(RemoteException re)
        {
            //System.out.println(re.getMessage());
            System.out.println("Nothing found :(");
        }
        if(sl!=null)
        {
            Iterator<Session> si = sl.iterator();
            while(si.hasNext())
            {
                Session sTemp = si.next();
                if(sTemp.getId().getGuid().equalsIgnoreCase(id))
                {
                    s = sTemp;
                    this.printObject(sTemp);
                }
            }
        }
        return s;
    }

    @Override
    public void printObject(Object o)
    {
        Session s = (Session)o;
        System.out.println("\nName: "+s.getName());
        System.out.println("Id: "+s.getId());
        System.out.println("ExId: "+s.getExternalId());
    }

    @Override
    public void updateExternalIdById(String id, String externalId)
    {
        Session s = (Session)this.getPanoptoObjectById(id);
        this.updateExternalId(s, externalId);
    }

    @Override
    public void updateExternalIdByName(String name, String externalId)
    {
        Session s = (Session)this.getPanoptoObjectByName(name);
        this.updateExternalId(s, externalId);
    }

    private void updateExternalId(Session s, String externalId)
    {
        if(s!=null)
        {
            UpdateSessionExternalId usei = new UpdateSessionExternalId();
            usei.setAuth(this.auth);
            usei.setExternalId(externalId);
            usei.setSessionId(s.getId());
            try
            {
                this.stub.updateSessionExternalId(usei);
            }
            catch(RemoteException re)
            {
                System.out.println(re.getMessage());
                return;
            }
            System.out.println("\nSession "+s.getName()+" updated with external Id "+externalId);
        }
        else
        {
            System.out.println("\nSession not found");
        }
    }
}
