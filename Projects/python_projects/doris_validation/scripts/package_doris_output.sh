#!/bin/bash

# sh package_doris_output.sh "/home/op/dingben/doris/output" "/home/op/dingben/doris_tars" "1.2.2-bin-intel-ctyunos" "doris"

set -eo pipefail

current_dir=$(
  cd $(dirname $0)
  pwd
)

output_root_path=$1
output_tar_dir=$2

postfix_tar_file_name=$3
prefix_tar_file_name=$4
if [ "$prefix_tar_file_name" = "" ]; then
  prefix_tar_file_name="doris-"
else
  prefix_tar_file_name=${prefix_tar_file_name}-
fi

cleanup() {
  echo "cleanup..."
  cd $output_root_path || exit 1
  fe_dir_name="${prefix_tar_file_name}fe-${postfix_tar_file_name}"
  if [[ -d "${fe_dir_name}" ]]; then
    echo "cleanup ${fe_dir_name}"
    mv ${fe_dir_name} fe
  fi

  be_dir_name="${prefix_tar_file_name}be-${postfix_tar_file_name}"
  if [[ -d "${be_dir_name}" ]]; then
    echo "cleanup ${be_dir_name}"
    mv ${be_dir_name} be
  fi

  dependencies_dir_name="${prefix_tar_file_name}dependencies-${postfix_tar_file_name}"
  if [[ -d "${dependencies_dir_name}" ]]; then
    echo "cleanup ${dependencies_dir_name}"
    mv ${dependencies_dir_name}/apache_hdfs_broker .
    mv ${dependencies_dir_name}/audit_loader .
    mv ${dependencies_dir_name}/jdbc .
    rmdir ${dependencies_dir_name}
  fi
}

trap cleanup SIGINT ERR EXIT

tar_all_output() {
  cd $output_root_path || exit 1

  tar_category=$1 # fe or be
  tar_dir_name=${prefix_tar_file_name}${tar_category}-${postfix_tar_file_name}
  tar_file_name=${tar_dir_name}.tar.gz

  mv $tar_category $tar_dir_name

  output_tar=${output_tar_dir}/${tar_file_name}
  rm -f $output_tar

  echo "Doing tar $tar_file_name to $output_tar_dir"
  tar -czf ${output_tar} $tar_dir_name

  mv $tar_dir_name $tar_category
}


tar_dependencies_output() {
  cd $output_root_path || exit 1

  tar_category="dependencies"
  tar_dir_name=${prefix_tar_file_name}${tar_category}-${postfix_tar_file_name}
  tar_file_name=${tar_dir_name}.tar.gz

  mkdir -p $tar_dir_name
  mv apache_hdfs_broker $tar_dir_name
  mv audit_loader $tar_dir_name
  mv jdbc $tar_dir_name

  output_tar=${output_tar_dir}/${tar_file_name}
  rm -f $output_tar

  echo "Doing tar $tar_file_name to $output_tar_dir"
  tar -czf ${output_tar} $tar_dir_name

  mv $tar_dir_name/apache_hdfs_broker .
  mv $tar_dir_name/audit_loader .
  mv $tar_dir_name/jdbc .
  rmdir $tar_dir_name
}

main() {
  mkdir -p $output_tar_dir
  tar_dependencies_output
  tar_all_output fe
  tar_all_output be
}

main
