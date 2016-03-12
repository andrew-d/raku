import React, { PropTypes } from 'react';
import { Link } from 'react-router';


export default class Navbar extends React.Component {
  static propTypes = {
    // Name of the app
    brand: PropTypes.string.isRequired,

    // Links to show
    links: PropTypes.arrayOf(PropTypes.shape({
      name: PropTypes.string.isRequired,
      link: PropTypes.string.isRequired,
    })).isRequired,
  }

  static contextTypes = {
    router: PropTypes.object.isRequired,
  }

  render() {
    return (
      <nav className='navbar navbar-default navbar-static-top'>
        <div className='container-fluid'>
          <div className='navbar-header'>
            {this._renderLink(this.props.brand, '/', 'navbar-brand')}
          </div>

          <ul className='nav navbar-nav'>
            {this._renderLinks()}
          </ul>
        </div>
      </nav>
    );
  }

  _renderLinks() {
    return this.props.links.map(spec => {
      const active = this.context.router.isActive(spec.link, true);
      const classes = active ? 'active' : '';

      return (
        <li className={classes} key={'link-' + spec.name}>
          {this._renderLink(spec.name, spec.link)}
        </li>
      );
    });
  }

  _renderLink(text, target, classes = '') {
    return <Link to={{pathname: target}} className={classes}>{text}</Link>;
  }
}
