import { useParams } from "react-router-dom";
import { useArticleById } from "../model/article";
import dayjs from "dayjs";
import {useMemo} from "react";

export default function ArticleDetail() {
  const { slug } = useParams();
  const { result } = useArticleById(slug);
  const updatedAt = useMemo(() => {
    return dayjs(result?.updatedAt).format("MMMM Do");
  }, [result?.updatedAt])

  return (
    <div className="article-page">
      <div className="banner">
        <div className="container">
          <h1 data-testid="article-title">{result?.title}</h1>

          <div className="article-meta">
            <a href="/profile/eric-simons">
              <img
                data-testid="article-author-image"
                src={result?.author.image}
              />
            </a>
            <div className="info">
              <a
                data-testid="article-author-username"
                href="/profile/eric-simons"
                className="author"
              >
                {result?.author.username}
              </a>
              <span data-testid="article-updatedAt" className="date">
                {updatedAt}
              </span>
            </div>
            <button className="btn btn-sm btn-outline-secondary">
              <i className="ion-plus-round"></i>
              &nbsp; Follow {result?.author.username}{" "}
              <span className="counter">(10)</span>
            </button>
            &nbsp;&nbsp;
            <button className="btn btn-sm btn-outline-primary">
              <i className="ion-heart"></i>
              &nbsp; Favorite Post{" "}
              <span data-testid="article-favoritesCount" className="counter">
                ({result?.favoritesCount})
              </span>
            </button>
            <button className="btn btn-sm btn-outline-secondary">
              <i className="ion-edit"></i> Edit Article
            </button>
            <button className="btn btn-sm btn-outline-danger">
              <i className="ion-trash-a"></i> Delete Article
            </button>
          </div>
        </div>
      </div>

      <div className="container page">
        <div className="row article-content" data-testid="article-content">
          <div className="col-md-12">
            <p data-testid="article-description">{result?.description}</p>
            <h2>Introducing RealWorld.</h2>
            <p>It's a great solution for learning how other frameworks work.</p>
            <ul className="tag-list" data-testid="article-tag-list">
              {result?.tagList.map((tag) => (
                <li key={tag} className="tag-default tag-pill tag-outline">
                  {tag}
                </li>
              ))}
            </ul>
          </div>
        </div>

        <hr />

        <div className="article-actions">
          <div className="article-meta">
            <a href="profile.html">
              <img src={result?.author.image} />
            </a>
            <div data-testid="article-author-username" className="info">
              <a href="" className="author">
                {result?.author.username}
              </a>
              <span className="date">{updatedAt}</span>
            </div>
            <button className="btn btn-sm btn-outline-secondary">
              <i className="ion-plus-round"></i>
              &nbsp; Follow {result?.author.username}
            </button>
            &nbsp;
            <button className="btn btn-sm btn-outline-primary">
              <i className="ion-heart"></i>
              &nbsp; Favorite Article{" "}
              <span data-testid="article-favoritesCount" className="counter">
                ({result?.favoritesCount})
              </span>
            </button>
            <button className="btn btn-sm btn-outline-secondary">
              <i className="ion-edit"></i> Edit Article
            </button>
            <button className="btn btn-sm btn-outline-danger">
              <i className="ion-trash-a"></i> Delete Article
            </button>
          </div>
        </div>

        <div className="row">
          <div className="col-xs-12 col-md-8 offset-md-2">
            <form className="card comment-form">
              <div className="card-block">
                <textarea
                  className="form-control"
                  placeholder="Write a comment..."
                  rows={3}
                ></textarea>
              </div>
              <div className="card-footer">
                <img
                  src={result?.author.image}
                  className="comment-author-img"
                />
                <button className="btn btn-sm btn-primary">Post Comment</button>
              </div>
            </form>

            <div className="card">
              <div className="card-block">
                <p className="card-text">
                  With supporting text below as a natural lead-in to additional
                  content.
                </p>
              </div>
              <div className="card-footer">
                <a href="/profile/author" className="comment-author">
                  <img
                    src="http://i.imgur.com/Qr71crq.jpg"
                    className="comment-author-img"
                  />
                </a>
                &nbsp;
                <a href="/profile/jacob-schmidt" className="comment-author">
                  Jacob Schmidt
                </a>
                <span className="date-posted">Dec 29th</span>
              </div>
            </div>

            <div className="card">
              <div className="card-block">
                <p className="card-text">
                  With supporting text below as a natural lead-in to additional
                  content.
                </p>
              </div>
              <div className="card-footer">
                <a href="/profile/author" className="comment-author">
                  <img
                    src="http://i.imgur.com/Qr71crq.jpg"
                    className="comment-author-img"
                  />
                </a>
                &nbsp;
                <a href="/profile/jacob-schmidt" className="comment-author">
                  Jacob Schmidt
                </a>
                <span className="date-posted">Dec 29th</span>
                <span className="mod-options">
                  <i className="ion-trash-a"></i>
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
