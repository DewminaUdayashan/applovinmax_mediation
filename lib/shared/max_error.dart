class MaxError {
  MaxError({
    required this.code,
    this.message = "",
  });
  final String code;
  final String message;

  factory MaxError.fromMap(Map<String, dynamic> map) {
    return MaxError(
      code: map['code'].toString(),
      message: map['message'].toString(),
    );
  }

  @override
  String toString() => 'MaxError(code: $code, message: $message)';
}
