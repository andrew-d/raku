import React from 'react';
import ReactDOM from 'react-dom';
import TestUtils from 'react-addons-test-utils';
import { expect } from 'chai';

import Home from '../../app/pages/Home';


describe('Home', function () {
  it('renders without problems', function () {
    const page = TestUtils.renderIntoDocument(<Home />);
    const title = TestUtils.findRenderedDOMComponentWithTag(page, 'h1');

    expect(ReactDOM.findDOMNode(title).textContent).to.eql('Raku');
  });
});
