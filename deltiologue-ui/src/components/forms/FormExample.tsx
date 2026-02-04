import Button from "./elements/Button";
import Label from "./elements/Label";
import TextField from "./elements/TextField";
import Title from "./elements/Title";
import Foot from "./elements/Foot";
import Form from "./elements/Form";
import TextArea from "./elements/TextArea";
import Section from "./elements/Section";
import FileUpload from "./elements/FileUpload";
import Select from "./elements/Select";
import Grid from "./elements/Grid";
import CheckBox from "./elements/CheckBox";
import Explanation from "./elements/Explanation";
import Paragraph from "./elements/Paragraph";
import FieldSet from "./elements/FieldSet";
import CheckBoxField from "./elements/CheckBoxField";
import Legend from "./elements/Legend";
import RadioButton from "./elements/RadioButton";
import RadioButtonGroup from "./elements/RadioButtonGroup";


export default function FormExample() {

  return (
    <Form>
      <Section>
        <Title>Profile</Title>
        <Paragraph>This information will be displayed publicly so be careful what you share.</Paragraph>

        <Grid>
          <div className="sm:col-span-4">
            <Label htmlFor="username">Username</Label>
            <TextField id="username" name="username" placeholder="janesmith" prefix="workcation.com/" />
          </div>

          <div className="col-span-full">
            <Label htmlFor="about">About</Label>
            <TextArea id="about" name="about" rows={3}></TextArea>
            <Paragraph>Write a few sentences about yourself.</Paragraph>
          </div>

          <div className="col-span-full">
            <Label htmlFor="about">Image Upload</Label>
            <FileUpload />
          </div>
        </Grid>
      </Section>

      <Section>
        <Grid>
          <div className="col-span-full">
            <Label htmlFor="country">Country</Label>
            <Select name="country">
              <option>Germany</option>
              <option>France</option>
              <option>Italy</option>
            </Select>
          </div>

          <div className="sm:col-span-3">
            <Label htmlFor="first-name">First name</Label>
            <TextField id="first-name" name="first-name" />
          </div>

          <div className="sm:col-span-3">
            <Label htmlFor="last-name">Last name</Label>
            <TextField id="last-name" name="last-name" />
          </div>
        </Grid>
      </Section>

      <Section>
        <Title>Notifications</Title>
        <Paragraph>
          We'll always let you know about important changes, but you pick what else you want to hear about.
        </Paragraph>

        <FieldSet>
          <Legend>By email</Legend>

          <CheckBoxField>
            <CheckBox id="comments" name="comments" />
            <Explanation>
              <Label htmlFor="comments">Comments</Label>
              <Paragraph>Get notified when someones posts a comment on a posting.</Paragraph>
            </Explanation>
          </CheckBoxField>

          <CheckBoxField>
            <CheckBox id="candidates" name="candidates" />
            <Explanation>
              <Label htmlFor="candidates">Candidates</Label>
              <Paragraph>Get notified when a candidate applies for a job.</Paragraph>
            </Explanation>
          </CheckBoxField>

          <CheckBoxField>
            <CheckBox id="offers" name="offers" />
            <Explanation>
              <Label htmlFor="offers">Offers</Label>
              <Paragraph>Get notified when a candidate accepts or rejects an offer.</Paragraph>
            </Explanation>
          </CheckBoxField>
        </FieldSet>

        <FieldSet>
          <Legend>Push Notifications</Legend>
          <Paragraph>These are delivered via SMS to your mobile phone.</Paragraph>

          <RadioButtonGroup>
            <RadioButton id="push-everything" name="push-notifications" defaultChecked>
              <Label htmlFor="push-everything">
                Nothing
              </Label>
            </RadioButton>

            <RadioButton id="push-email" name="push-notifications">
              <Label htmlFor="push-email">
                Same as email
              </Label>
            </RadioButton>

            <RadioButton id="push-nothing" name="push-notifications">
              <Label htmlFor="push-nothing">
                No push notifications
              </Label>
            </RadioButton>

          </RadioButtonGroup>
        </FieldSet>

      </Section>

      <Foot>
        <Button secondary type="button">Cancel</Button>
        <Button primary type="submit">Save</Button>
      </Foot>
    </Form>
  );
}