package edu.rpi.metpetdb.client.ui.user;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.objects.list.ProjectSampleList;
import edu.rpi.metpetdb.client.ui.objects.list.SampleList;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class ProjectSamplesList extends MPagePanel {
		private final SampleList list;
		
		public ProjectSamplesList(final long projectId) {
			list = new ProjectSampleList() {
				public void update(final PaginationParameters p,
						final AsyncCallback<Results<Sample>> ac) {
					MpDb.sample_svc.projectSamples(p, projectId, ac);
				}

				@Override
				public void getAllIds(AsyncCallback<Map<Object, Boolean>> ac) {
					long id = (long) (MpDb.currentUser().getId());
					MpDb.sample_svc.allIdsForUser(id,ac);	
				}


			};
			addPageHeader();
			((ProjectSampleList)list).setProjectId(projectId);
			add(list);
		}
		
		public void reload() {
			list.getScrollTable().reloadPage();
		}

		protected void addPageHeader() {
			setPageTitle("Project Samples");
		}

	}
