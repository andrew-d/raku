import React, { PropTypes } from 'react';

import Navbar from 'components/Navbar';


export default class App extends React.Component {
  static propTypes = {
    children: PropTypes.any,
  }

  render() {
    return (
      <div>
        <div className='page-wrapper'>
          <Navbar
            brand='Raku'
            links={[
              { name: 'Home', link: '/' },
              { name: 'Tags', link: '/tags' },
              { name: 'About', link: '/about' },
            ]}
          />

          <div className='container-fluid'>
            {this.props.children}
          </div>
        </div>

        {this._renderDevTools()}
      </div>
    );
  }

  // Render Redux DevTools only in non-production builds.
  _renderDevTools() {
    if (process.env.NODE_ENV !== 'production') {
      const DevTools = require('components/DevTools').default;
      return <DevTools />;
    }

    return null;
  }
}
