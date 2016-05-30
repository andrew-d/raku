import React, { PropTypes } from 'react';
import { Link } from 'react-router';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';

import { fetchTag } from 'redux/modules/tags';
import isLoading from 'utils/isLoading';


function strictToNumber(s, def=null) {
  if (!s) {
    return def;
  }

  const num = +s;
  if (isNaN(num)) {
    return def;
  }

  return num;
}

export class Tag extends React.Component {
  static propTypes = {
    // Data
    tag: PropTypes.object,
    loading: PropTypes.bool.isRequired,
    routeParams: PropTypes.shape({
      id: PropTypes.string.isRequired,
    }).isRequired,

    // Actions
    fetchTag: PropTypes.func.isRequired,
  }

  static contextTypes = {
    router: PropTypes.object.isRequired,
  }

  // Fetch the current tag when this component loads.
  componentWillMount() {
    const tagId = strictToNumber(this.props.routeParams.id);
    if (tagId) {
      this.props.fetchTag(tagId);
    }
  }

  // Whenever we get new properties, if the tag ID has changed, we
  // re-fetch it from the server.
  componentWillReceiveProps(nextProps) {
    const currId = strictToNumber(this.props.routeParams.id),
      nextId = strictToNumber(nextProps.routeParams.id);

    if (!nextId || !currId) {
      return;
    }

    if (nextId !== currId) {
      this.props.fetchTag(nextId);
    }
  }

  render() {
    const tagId = strictToNumber(this.props.routeParams.id);
    if (!tagId) {
      return <h3>Invalid tag id</h3>;
    }

    if (this.props.loading) {
      return <i>Loading...</i>;
    }
    if (!this.props.tag) {
      return <i>No such tag</i>;
    }

    return (
      <div className='row'>
        <div className='col-sm-12'>
          <h1>Tag '{this.props.tag.name}'</h1>
        </div>
      </div>
    );
  }

  renderTags() {
    if (!this.props.tag.documents) {
      return null;
    }

    return <div>TODO</div>;
  }
}

function mapStateToProps(state, ownProps) {
  const tagId = ownProps.routeParams.id;
  const tag = state.tags.tags[tagId];

  return {
    tag: tag,
    documents: state.documents.documents,
    loading: isLoading(tag),
  };
}

export default connect(mapStateToProps, { fetchTag })(Tag);
