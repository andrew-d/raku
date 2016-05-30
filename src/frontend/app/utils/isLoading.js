export function isLoading(obj) {
  if (obj === undefined || obj === null) {
    return true;
  }

  if (obj.$loading === undefined) {
    return false;
  }

  return obj.$loading;
}

export default isLoading;
