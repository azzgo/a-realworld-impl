import React, { useContext, useState } from "react";
import { Input } from "../components/Input";
import { Textarea } from "../components/Textarea";
import Form, { Field } from "rc-field-form";
import { ArticleControllerContext } from "../model/article";
import {useParams} from "react-router";

interface Props {}

type ArticleFormValueType = {
  title: string;
  description: string;
  body: string;
}

const ArticleEditor: React.FC<Props> = () => {
  const [formRef] = Form.useForm();
  const params = useParams();
  const articleController = useContext(ArticleControllerContext);
  const [tags, setTags] = useState<string[]>([]);
  const [tagInput, setTagInput] = useState("");

  const handleTagInput = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTagInput(event.target.value);
  };

  const handleAddTag = () => {
    if (tagInput && !tags.includes(tagInput)) {
      setTags([...tags, tagInput]);
      setTagInput("");
    }
  };

  const handleRemoveTag = (tag: string) => {
    setTags(tags.filter((t) => t !== tag));
  };

  const handleSubmit = (values: ArticleFormValueType) => {
    console.log(params)
    const isEdit = !!params.slug;
    const saveOrUpdatedFormData = {
      ...values,
      tagList: tags,
    }
    if (isEdit) {
      articleController?.update(params.slug!, saveOrUpdatedFormData);
    } else {
      articleController?.create(saveOrUpdatedFormData);
    }
  };

  return (
    <div className="editor-page">
      <div className="container page">
        <div className="row">
          <div className="col-md-10 offset-md-1 col-xs-12">
            {/* <ul className="error-messages">
              <li>{"That title is required"}</li>
            </ul> */}

            <Form form={formRef} onFinish={handleSubmit}>
              <fieldset>
                <fieldset className="form-group">
                  <Field name="title">
                    <Input
                      type="text"
                      className="form-control form-control-lg"
                      placeholder="Article Title"
                      data-testid="article-title"
                    />
                  </Field>
                </fieldset>
                <fieldset className="form-group">
                  <Field name="description">
                    <Input
                      type="text"
                      className="form-control"
                      placeholder="What's this article about?"
                      data-testid="article-description"
                    />
                  </Field>
                </fieldset>
                <fieldset className="form-group">
                  <Field name="body">
                    <Textarea
                      className="form-control"
                      rows={8}
                      placeholder="Write your article (in markdown)"
                      data-testid="article-body"
                    />
                  </Field>
                </fieldset>
                <fieldset className="form-group">
                  <Input
                    type="text"
                    className="form-control"
                    placeholder="Enter tags"
                    value={tagInput}
                    data-testid="article-tag"
                    onChange={handleTagInput}
                    onKeyDown={(event: KeyboardEvent) => {
                      if (event.key === "Enter") {
                        event.preventDefault();
                        handleAddTag();
                      }
                    }}
                  />
                  <div className="tag-list">
                    {tags.map((tag) => (
                      <span className="tag-default tag-pill" key={tag}>
                        {tag}{" "}
                        <i
                          className="ion-close-round"
                          onClick={() => handleRemoveTag(tag)}
                        ></i>
                      </span>
                    ))}
                  </div>
                </fieldset>
                <button
                  className="btn btn-lg pull-xs-right btn-primary"
                  type="submit"
                  data-testid="publish-button"
                >
                  Publish Article
                </button>
              </fieldset>
            </Form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ArticleEditor;
