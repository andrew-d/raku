import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import { push } from 'react-router-redux';

import Navbar from 'components/Navbar';


export class App extends React.Component {
  static propTypes = {
    children: PropTypes.any,
    currentPath: PropTypes.string.isRequired,
    onNavigate: PropTypes.func.isRequired,
  }

  render() {
    return (
      <div>
        <div className='page-wrapper'>
          <Navbar
            brand="Raku"
            links={[
              { name: "Home", link: "/" },
              { name: "About", link: "/about" },
            ]}
            current={this.props.currentPath}
            onClick={this.props.onNavigate}
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

function mapStateToProps(state, ownProps) {
  return {
    currentPath: ownProps.location.pathname,
  };
};

function mapDispatchToProps(dispatch, ownProps) {
  return {
    onNavigate: (path) => dispatch(push(path)),
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(App);
