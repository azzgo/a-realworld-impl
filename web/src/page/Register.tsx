import Form, { Field } from "rc-field-form";
import { Input } from "../components/Input";
import { UserControllerContext } from "../model/user";
import { useContext } from "react";
import { useNavigate } from "react-router";

type RegisterFormData = {
  username: string;
  email: string;
  password: string;
};

export default function Register() {
  const userController = useContext(UserControllerContext);
  const [formRef] = Form.useForm<RegisterFormData>();
  const navigate = useNavigate();

  const summit = (values: RegisterFormData) => {
    userController?.register(values.email, values.username, values.password).then(
      () => navigate("/"),
    );
  };
  
  return (
    <div className="auth-page">
      <div className="container page">
        <div className="row">
          <div className="col-md-6 offset-md-3 col-xs-12">
            <h1 className="text-xs-center">Sign up</h1>
            <p className="text-xs-center">
              <a href="/login">Have an account?</a>
            </p>

            <ul className="error-messages">
              <li>That email is already taken</li>
            </ul>

            <Form form={formRef} onFinish={summit}>
              <fieldset className="form-group">
                <Field name="username">
                  <Input
                    className="form-control form-control-lg"
                    type="text"
                    required
                    data-testid="username-input"
                    placeholder="Username"
                  />
                </Field>
              </fieldset>
              <fieldset className="form-group">
                <Field name="email">
                  <Input
                    className="form-control form-control-lg"
                    type="email"
                    required
                    data-testid="email-input"
                    placeholder="Email"
                  />
                </Field>
              </fieldset>
              <fieldset className="form-group">
                <Field name="password">
                  <Input
                    className="form-control form-control-lg"
                    type="password"
                    required
                    data-testid="password-input"
                    placeholder="Password"
                  />
                </Field>
              </fieldset>
              <button
                data-testid="submit-button"
                type="submit"
                className="btn btn-lg btn-primary pull-xs-right"
              >
                Sign up
              </button>
            </Form>
          </div>
        </div>
      </div>
    </div>
  );
}
