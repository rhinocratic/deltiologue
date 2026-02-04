import Button from "../form_elements/Button";
import Label from "../form_elements/Label";
import TextField from "../form_elements/TextField";
import Title from "../form_elements/Title";
import Foot from "../form_elements/Foot";
import Form from "../form_elements/Form";
import TextArea from "../form_elements/TextArea";
import Section from "../form_elements/Section";
import FileUpload from "../form_elements/FileUpload";
import Select from "../form_elements/Select";
import Grid from "../form_elements/Grid";
import CheckBox from "../form_elements/CheckBox";
import Explanation from "../form_elements/Explanation";
import Paragraph from "../form_elements/Paragraph";
import FieldSet from "../form_elements/FieldSet";
import CheckBoxField from "../form_elements/CheckBoxField";
import Legend from "../form_elements/Legend";
import RadioButton from "../form_elements/RadioButton";
import RadioButtonGroup from "../form_elements/RadioButtonGroup";


export default function ExampleForm() {

  return (
    <Form>
      <Section>
        <Title>Profile</Title>
        <Grid>
          <div className="sm:col-span-4">
            <Label htmlFor="username">Username</Label>
            <TextField id="username" name="username" placeholder="janesmith" prefix="workcation.com/" />
          </div>

          <div className="col-span-full">
            <Label htmlFor="about">About</Label>
            <TextArea id="about" name="about" rows={3}></TextArea>
            <p className="mt-3 text-sm/6 text-gray-600">Write a few sentences about yourself.</p>
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