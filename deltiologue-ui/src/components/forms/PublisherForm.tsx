import { FunctionComponent, useState } from "react";
import Button from "./elements/Button";
import Label from "./elements/Label";
import TextField from "./elements/TextField";
import Title from "../text/Title";
import Foot from "./elements/Foot";
import Form from "./elements/Form";
import Section from "./elements/Section";
import FormFields from "./elements/FormFields";

type PublisherFormProps = {
  name?: string;
}

const PublisherForm: FunctionComponent<PublisherFormProps> = ({ name }) => {

  const [publisherName, setPublisherName] = useState(name);

  const handleSetName = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPublisherName(event.target.value);
  }

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // submit
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Section>
        <Title>Create a new publisher</Title>
        <FormFields>
          <div className="sm:col-span-3">
            <Label htmlFor="publisher-name">Name</Label>
            <TextField
              id="publisher-name"
              name="publisher-name"
              placeholder="publisher name"
              value={publisherName}
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

export default PublisherForm;