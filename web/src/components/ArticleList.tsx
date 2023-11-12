import { Article } from "../model/article";
import { ArticleListItem } from "./ArticleListItem";

export interface ArticleListProps {
  items: Article[];
  // total: number;
  // page: number;
  // onPageChange: (page: number) => void;
}

export default function ArticleList({
  items = [],
}: ArticleListProps): JSX.Element {
  return (
    <>
      {items.map((item) => (
        <ArticleListItem article={item} key={item.slug} />
      ))}

      <ul className="pagination">
        <li className="page-item active">
          <a className="page-link" href="">
            1
          </a>
        </li>
        <li className="page-item">
          <a className="page-link" href="">
            2
          </a>
        </li>
      </ul>
    </>
  );
}
