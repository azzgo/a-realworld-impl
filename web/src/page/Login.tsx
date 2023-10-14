import { useContext, useState } from "react";
import { UserControllerContext } from "../model/user";
import Form, { Field } from "rc-field-form";
import { useNavigate } from "react-router";
import { Input } from "../components/Input";
import { AxiosError } from "axios";
import { ErrorBody } from "../type";
import { ErrorCodeKey } from "../utils/request";

type LoginFormData = {
  email: string;
  password: string;
};

export default function Login() {
  const userController = useContext(UserControllerContext);
  const [formRef] = Form.useForm<LoginFormData>();
  const navigate = useNavigate();
  const [showFormError, setShowFormError] = useState<string[]>([]);

  const summit = (values: LoginFormData) => {
    userController?.login(values.email, values.password).then(
      () => navigate("/"),
      (e: AxiosError<ErrorBody>) => {
        if (e.response?.data?.errors?.[ErrorCodeKey.emailOrPasssword]) {
          const errorReason =
            e.response?.data.errors[ErrorCodeKey.emailOrPasssword];
          setShowFormError([
            `${ErrorCodeKey.emailOrPasssword}} ${errorReason}}`,
          ]);
        }
      }
    );
  };

  return (
    <div className="auth-page">
      <div className="container page">
        <div className="row">
          <div className="col-md-6 offset-md-3 col-xs-12">
            <h1 className="text-xs-center">Sign in</h1>
            <p className="text-xs-center">
              <a href="/register">Need an account?</a>
            </p>

            {showFormError.length > 0 && (
              <ul className="error-messages" data-testid="error-messages">
                {showFormError.map((it) => (
                  <li>{it}</li>
                ))}
              </ul>
            )}

            <Form form={formRef} onFinish={summit}>
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
                Sign in
              </button>
            </Form>
          </div>
        </div>
      </div>
    </div>
  );
}
