import { Link } from "react-router-dom";

export default function AuthenticatedHeader() {
  return (
    <nav className="navbar navbar-light">
      <div className="container">
        <a className="navbar-brand" href="/">
          conduit
        </a>
        <ul className="nav navbar-nav pull-xs-right">
          <li className="nav-item">
            <Link className="nav-link active" to="/">
              Home
            </Link>
          </li>
          <li className="nav-item">
            <Link
              className="nav-link"
              data-testid="create-article"
              to="/editor"
            >
              {" "}
              <i className="ion-compose"></i>&nbsp;New Article{" "}
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/settings">
              {" "}
              <i className="ion-gear-a"></i>&nbsp;Settings{" "}
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/profile/eric-simons">
              <img src="" className="user-pic" />
              Eric Simons
            </Link>
          </li>
        </ul>
      </div>
    </nav>
  );
}
