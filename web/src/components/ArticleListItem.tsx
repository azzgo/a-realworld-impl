import { Article } from "../model/article";

export interface ArticleListItemProps {
  article: Article;
}

export function ArticleListItem({
  article,
}: ArticleListItemProps): JSX.Element {
  return (
    <div className="article-preview">
      <div className="article-meta">
        <a href="/profile/eric-simons">
          <img src={article.author.image} />
        </a>
        <div className="info">
          <a href="/profile/eric-simons" className="author">
            {article.author.username}
          </a>
          <span className="date">January 20th</span>
        </div>
        <button className="btn btn-outline-primary btn-sm pull-xs-right">
          <i className="ion-heart"></i> 29
        </button>
      </div>
      <a
        href="/article/how-to-build-webapps-that-scale"
        className="preview-link"
      >
        <h1>{article.title}</h1>
        <p>{article.description}</p>
        <span>Read more...</span>
        <ul className="tag-list">
          {article.tagList.map((tag) => (
            <li className="tag-default tag-pill tag-outline" key={tag}>
              {tag}
            </li>
          ))}
        </ul>
      </a>
    </div>
  );
}
