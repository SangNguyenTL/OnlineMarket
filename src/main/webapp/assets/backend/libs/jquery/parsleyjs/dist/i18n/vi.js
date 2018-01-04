// This is included with the Parsley library itself,
// thus there is no use in adding it to your project.


Parsley.addMessages('vi', {
  defaultMessage: "Giá trị này dường như chưa hợp lệ.",
  type: {
    email:        "Giá trị này phải là một email hợp lệ.",
    url:          "Giá trị này phải là một url hợp lệ.",
    number:       "Giá trị này phải là một số hợp lệ.",
    integer:      "Giá trị này phải là một số nguyên.",
    digits:       "Giá trị này phải là số.",
    alphanum:     "Giá trị này không được chứa ký tự đặc biệt ngoài chữ và số."
  },
  notblank:       "Giá trị này không được để trống",
  required:       "Giá trị này phải được điền.",
  pattern:        "Giá trị này dường như chưa hợp lệ.",
  min:            "Giá trị này phải lớn hơn hoặc bằng với %s.",
  max:            "Giá trị này phải nhỏ hơn hoặc bằng với %s.",
  range:          "Giá trị này phải từ %s tới %s.",
  minlength:      "Giá trị này quá ngắn. Nó phải có từ %s ký tự trở lên.",
  maxlength:      "Giá trị này quá dài. Nó phải có ít hơn %s ký tự trở xuống.",
  length:         "Giá trị này chưa hợp lệ. Nó phải từ %s tới %s ký tự.",
  mincheck:       "Bạn phải chọn ít nhất %s sự lựa chọn.",
  maxcheck:       "Bạn chỉ được chọn từ %s sự lượng chọn trở xuống.",
  check:          "Bạn phải chọn từ %s tới %s sự lựa chọn.",
  equalto:        "Giá trị này phải giống nhau."
});

Parsley.setLocale('vi');
