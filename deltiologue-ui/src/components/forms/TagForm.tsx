import { FunctionComponent, useState } from "react";
import Button from "./elements/Button";
import Label from "./elements/Label";
import TextField from "./elements/TextField";
import Title from "../text/Title";
import Foot from "./elements/Foot";
import Form from "./elements/Form";
import Section from "./elements/Section";
import FormFields from "./elements/FormFields";

type TagFormProps = {
  name: string;
}

const TagForm: FunctionComponent<TagFormProps> = ({ name }) => {

  const [categoryName, setCategoryName] = useState(name);

  const handleSetName = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCategoryName(event.target.value);
  }

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // submit
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Section>
        <Title>Create a new tag</Title>
        <FormFields>
          <div className="sm:col-span-3">
            <Label htmlFor="tag-name">Name</Label>
            <TextField
              id="tag-name"
              name="tag-name"
              placeholder="tag name"
              value={categoryName}
              onChange={handleSetName}
            />
          </div>
        </FormFields>
      </Section>

      <Foot>
        <Button secondary type="button">Cancel</Button>
        <Button primary type="submit">Save</Button>
      </Foot>
    </Form>
  );
}

export default TagForm;