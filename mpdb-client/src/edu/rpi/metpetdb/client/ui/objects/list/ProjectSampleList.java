package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public class ProjectSampleList extends SampleList {
	
	private long projectId;

	/**
	 * ProjectSampleList differs from SampleList only by the actions available
	 * in the header. i.e. one would only see "Remove from project" while looking
	 * at a sample in the context of a project.
	 */
	public void initialize() {
		super.initialize();
		setTableActions(new SampleListActions(this, true));
	}
	
	public void setProjectId(long id){
		projectId = id;
	}
	
	public long getProjectId(){
		return projectId;
	}
	
	@Override
	public void getAllIds(AsyncCallback<Map<Object, Boolean>> ac) {
	}

	@Override
	public void update(PaginationParameters p, AsyncCallback<Results<Sample>> ac) {
	}

}
