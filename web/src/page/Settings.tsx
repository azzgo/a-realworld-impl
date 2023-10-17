import { useContext } from "react";
import { UpdatedUserInfo, UserControllerContext, userAtom } from "../model/user";
import { useNavigate } from "react-router";
import { useAtom } from "jotai";
import Form, { Field } from "rc-field-form";
import { Input } from "../components/Input";
import { Textarea } from "../components/Textarea";

export default function Settings() {
  const userController = useContext(UserControllerContext);
  const navigate = useNavigate();
  const [user] = useAtom(userAtom);
  const [formRef] = Form.useForm();

  function logout() {
    userController?.logout();
    navigate("/");
  }

  function summit(values: UpdatedUserInfo) {
    userController?.update(values);
  }

  return (
    <div className="settings-page">
      <div className="container page">
        <div className="row">
          <div className="col-md-6 offset-md-3 col-xs-12">
            <h1 className="text-xs-center">Your Settings</h1>

            <ul className="error-messages">
              <li>That name is required</li>
            </ul>

            <Form initialValues={user ?? {}} form={formRef} onFinish={summit}>
              <fieldset>
                <fieldset className="form-group">
                  <Field name="image">
                    <Input
                      className="form-control"
                      type="text"
                      data-testid="profile-image"
                      placeholder="URL of profile picture"
                    />
                  </Field>
                </fieldset>
                <fieldset className="form-group">
                  <Field name="username">
                    <Input
                      className="form-control form-control-lg"
                      type="text"
                      data-testid="profile-username"
                      placeholder="Your Name"
                    />
                  </Field>
                </fieldset>
                <fieldset className="form-group">
                  <Field name="bio">
                    <Textarea
                      className="form-control form-control-lg"
                      rows={8}
                      data-testid="profile-bio"
                      placeholder="Short bio about you"
                    />
                  </Field>
                </fieldset>
                <fieldset className="form-group">
                  <Field name="email">
                    <input
                      className="form-control form-control-lg"
                      type="email"
                      data-testid="profile-email"
                      placeholder="Email"
                    />
                  </Field>
                </fieldset>
                <fieldset className="form-group">
                  <Field name="password">
                    <input
                      className="form-control form-control-lg"
                      type="password"
                      data-testid="profile-password"
                      placeholder="New Password"
                    />
                  </Field>
                </fieldset>
                <button
                  className="btn btn-lg btn-primary pull-xs-right"
                  data-testid="update-btn"
                  type="submit"
                >
                  Update Settings
                </button>
              </fieldset>
            </Form>
            <hr />
            <button
              onClick={logout}
              className="btn btn-outline-danger"
              data-testid="logout-btn"
            >
              Or click here to logout.
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
