package edu.rpi.metpetdb.gwt.client.screen;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.validation.client.InvalidConstraint;
import com.google.gwt.validation.client.interfaces.IValidator;

import edu.rpi.metpetdb.gwt.client.LoginService;
import edu.rpi.metpetdb.gwt.client.LoginServiceAsync;
import edu.rpi.metpetdb.gwt.client.dto.LoginRequestDTO;
import edu.rpi.metpetdb.gwt.client.dto.UserDTO;

public class LoginScreen extends AbsolutePanel {

	private final TextBox username = new TextBox();

	private final PasswordTextBox password = new PasswordTextBox();

	private final DialogBox loginDlg = new DialogBox(true, true);

	private final FlowPanel flowPanel = new FlowPanel();

	private final UserDTO userDTO = null;
	
	private final LoginServiceAsync loginService = (LoginServiceAsync) GWT
	.create(LoginService.class);

	public LoginScreen() {


		flowPanel.add(username);
		flowPanel.add(password);
		flowPanel.add(new Button("OK", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				LoginRequestDTO loginRequestDTO = buildTransferObject(username
						.getValue(), password.getValue());
				IValidator<LoginRequestDTO> validateLoginRequestDTO = GWT
						.create(LoginRequestDTO.class);

				Set<InvalidConstraint<LoginRequestDTO>> errors = validateLoginRequestDTO
						.validate(loginRequestDTO);

				if (errors.size() == 0) {

					loginService.login(loginRequestDTO,
							new AsyncCallback<UserDTO>() {

								@Override
								public void onSuccess(UserDTO result) {
									// TODO Auto-generated method stub
								//	this.userDTO = result;
								//	Window.alert(response);
							
									
									RootPanel.get("main").clear();
									RootPanel.get("main").add(new UploadScreen(result));
								}

								@Override
								public void onFailure(Throwable caught) {
								//	this.userDTO = caught.getMessage();
									Window.alert(caught.getMessage());
								}
							});

				} else
					flowPanel.add(new HTML(errors.toString()));

			}

		}));

		flowPanel.add(new Button("Cancel", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				username.setValue("");
				password.setValue("");

			}
		}));

		loginDlg.add(flowPanel);

		this.add(loginDlg);
	}

	public LoginRequestDTO buildTransferObject(String username, String password) {
		return new LoginRequestDTO(username, password);
	}
}
