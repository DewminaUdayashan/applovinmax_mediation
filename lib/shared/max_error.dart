class MaxError {
  MaxError(
    this.code,
    this.message,
  );
  final String code;
  final String message;

  factory MaxError.fromMap(Map<String, dynamic> map) {
    return MaxError(
      map['code'] as String,
      map['message'] as String,
    );
  }

  @override
  String toString() => 'MaxError(code: $code, message: $message)';
}
