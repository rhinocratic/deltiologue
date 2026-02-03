import { render, screen } from '@testing-library/react';
import { describe, test } from 'vitest';

import App from '../src/App';

describe('App', () => {
  test('renders headline', () => {
    render(<App />);

    screen.debug();

    // check if App components renders headline
  });
});